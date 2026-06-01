import os
import sys
import re
import argparse
import matplotlib.pyplot as plt
import numpy as np
from pathlib import Path


class CSVPlotter:
    def __init__(self, csv_dir='../csv'):
        self.csv_dir = Path(csv_dir)
        if not self.csv_dir.exists():
            raise FileNotFoundError(f"Directory '{csv_dir}' not found")

        self.data = {}
        self.available_files = self._find_csv_files()

    def _find_csv_files(self):
        files = {}
        for csv_file in self.csv_dir.glob('*.csv'):
            name = csv_file.stem
            files[name] = csv_file
        return files

    def _parse_european_format(self, value_str):
        try:
            normalized = value_str.replace(',', '.')
            return float(normalized)
        except ValueError:
            return None

    def load_csv(self, function_name):
        if function_name not in self.available_files:
            raise FileNotFoundError(
                f"Function '{function_name}' not found. "
                f"Available: {', '.join(self.available_files.keys())}"
            )

        filepath = self.available_files[function_name]
        x_values = []
        y_values = []

        with open(filepath, 'r', encoding='utf-8') as f:
            lines = f.readlines()

            if lines and lines[0].strip().lower() == 'x;result':
                lines = lines[1:]

            for line in lines:
                line = line.strip()
                if not line:
                    continue

                parts = line.split(';')
                if len(parts) != 2:
                    continue

                x = self._parse_european_format(parts[0])
                y = self._parse_european_format(parts[1])

                if x is None or y is None:
                    continue

                x_values.append(x)
                y_values.append(y)

        if not x_values:
            raise ValueError(f"No valid data found in {filepath}")

        self.data[function_name] = {
            'x': np.array(x_values),
            'y': np.array(y_values)
        }
        return self.data[function_name]

    def _prepare_plot_series(self, x, y):
        # Ensure points are drawn in x order.
        sort_idx = np.argsort(x)
        x_sorted = x[sort_idx]
        y_sorted = y[sort_idx].astype(float)

        # Drop invalid/extreme values first.
        y_plot = np.where(np.isfinite(y_sorted), y_sorted, np.nan)
        y_plot = np.where(np.abs(y_plot) > 1e6, np.nan, y_plot)

        finite_idx = np.where(np.isfinite(y_plot))[0]
        if finite_idx.size > 1:
            y_finite = y_plot[finite_idx]
            dy = np.abs(np.diff(y_finite))

            if dy.size > 0:
                robust = np.nanmedian(dy)
                if not np.isfinite(robust) or robust <= 0:
                    robust = np.nanpercentile(dy, 75)
                if not np.isfinite(robust) or robust <= 0:
                    robust = 1.0

                jump_threshold = max(20.0, robust * 25.0)

                for j, delta in enumerate(dy):
                    i_prev = finite_idx[j]
                    i_curr = finite_idx[j + 1]

                    prev_abs = abs(y_plot[i_prev])
                    curr_abs = abs(y_plot[i_curr])
                    sign_flip = np.sign(y_plot[i_prev]) != np.sign(y_plot[i_curr])
                    near_asymptote = prev_abs > 5.0 or curr_abs > 5.0

                    # Break line across likely asymptotes/discontinuities.
                    if (delta > jump_threshold and near_asymptote) or (
                        sign_flip and prev_abs > 8.0 and curr_abs > 8.0
                    ):
                        y_plot[i_curr] = np.nan

        y_limits = None
        finite_vals = y_plot[np.isfinite(y_plot)]
        if finite_vals.size > 10:
            low, high = np.percentile(finite_vals, [2, 98])
            span = high - low
            if span > 0:
                pad = span * 0.1
                y_limits = (low - pad, high + pad)

        return x_sorted, y_plot, y_limits

    def plot_single(self, function_name, output_file=None,
                   title=None, xlabel='x', ylabel='y',
                   grid=True, figsize=(10, 6)):
        if function_name not in self.data:
            self.load_csv(function_name)

        data = self.data[function_name]

        plt.figure(figsize=figsize)

        x, y_plot, y_limits = self._prepare_plot_series(data['x'], data['y'])
        plt.plot(x, y_plot, linewidth=2, label=function_name)

        if y_limits is not None:
            plt.ylim(*y_limits)

        if title is None:
            title = f"{function_name.replace('_', ' ').title()}"

        plt.title(title, fontsize=14, fontweight='bold')
        plt.xlabel(xlabel, fontsize=12)
        plt.ylabel(ylabel, fontsize=12)
        plt.legend(fontsize=11)

        if grid:
            plt.grid(True, alpha=0.3, linestyle='--')

        plt.tight_layout()

        if output_file is None:
            output_file = f"plot_{function_name}.png"

        plt.savefig(output_file, dpi=150, bbox_inches='tight')
        print(f"✓ Saved: {output_file}")
        plt.close()

    def plot_multiple(self, function_names, output_file=None,
                     title=None, xlabel='x', ylabel='y',
                     grid=True, figsize=(12, 7)):
        for fname in function_names:
            if fname not in self.data:
                self.load_csv(fname)

        plt.figure(figsize=figsize)

        colors = plt.cm.tab10(np.linspace(0, 1, len(function_names)))
        combined_finite = []

        for fname, color in zip(function_names, colors):
            data = self.data[fname]
            x, y_plot, _ = self._prepare_plot_series(data['x'], data['y'])
            plt.plot(x, y_plot, linewidth=2, label=fname, color=color)

            finite_vals = y_plot[np.isfinite(y_plot)]
            if finite_vals.size > 0:
                combined_finite.append(finite_vals)

        if combined_finite:
            merged = np.concatenate(combined_finite)
            if merged.size > 10:
                low, high = np.percentile(merged, [2, 98])
                span = high - low
                if span > 0:
                    pad = span * 0.1
                    plt.ylim(low - pad, high + pad)

        if title is None:
            title = f"Functions: {', '.join(function_names)}"

        plt.title(title, fontsize=14, fontweight='bold')
        plt.xlabel(xlabel, fontsize=12)
        plt.ylabel(ylabel, fontsize=12)
        plt.legend(fontsize=10, loc='best')

        if grid:
            plt.grid(True, alpha=0.3, linestyle='--')

        plt.tight_layout()

        if output_file is None:
            output_file = "plot_comparison.png"

        plt.savefig(output_file, dpi=150, bbox_inches='tight')
        print(f"✓ Saved: {output_file}")
        plt.close()

    def plot_by_category(self, category, output_file=None, figsize=(14, 8)):
        category_map = {
            'trig': ['sin', 'cos', 'tan', 'cot', 'sec', 'csc'],
            'log': ['ln', 'log2', 'log5', 'log10'],
            'system': ['system_negative', 'system_positive']
        }

        if category.lower() not in category_map:
            raise ValueError(f"Unknown category: {category}. "
                           f"Available: {', '.join(category_map.keys())}")

        functions = category_map[category.lower()]
        available = [f for f in functions if f in self.available_files]

        if not available:
            print(f"No functions found for category '{category}'")
            return

        title_map = {
            'trig': 'Trigonometric Functions (x ≤ 0)',
            'log': 'Logarithmic Functions (x > 0)',
            'system': 'Main System Function'
        }

        title = title_map.get(category.lower(), f'{category} Functions')

        self.plot_multiple(available, output_file=output_file,
                         title=title, figsize=figsize)

    def plot_all(self):
        for fname in sorted(self.available_files.keys()):
            print(f"Plotting {fname}...", end=' ')
            self.plot_single(fname)

    def list_available(self):
        if not self.available_files:
            print("No CSV files found")
            return

        print("Available CSV files:")
        for fname in sorted(self.available_files.keys()):
            filepath = self.available_files[fname]
            size = filepath.stat().st_size
            print(f"  • {fname:<20} ({size:>6} bytes)")


def main():
    parser = argparse.ArgumentParser(
        description='Plot mathematical functions from CSV files',
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Examples:
  # List available functions
  python plot_csv.py --list

  # Plot single function
  python plot_csv.py --plot sin

  # Plot multiple functions
  python plot_csv.py --plot sin cos tan

  # Plot by category
  python plot_csv.py --category trig
  python plot_csv.py --category log
  python plot_csv.py --category system

  # Plot all functions
  python plot_csv.py --all

  # Custom output file
  python plot_csv.py --plot sin --output my_sine_plot.png

  # Specify CSV directory
  python plot_csv.py --csv-dir ./csv --plot sin
        """
    )

    parser.add_argument('--list', action='store_true',
                       help='List available CSV files')
    parser.add_argument('--plot', nargs='+', metavar='FUNCTION',
                       help='Plot specific functions')
    parser.add_argument('--category', choices=['trig', 'log', 'system'],
                       help='Plot all functions in a category')
    parser.add_argument('--all', action='store_true',
                       help='Plot all available functions individually')
    parser.add_argument('--output', '-o', metavar='FILE',
                       help='Output file name (for single/category plots)')
    parser.add_argument('--csv-dir', default='csv',
                       help='Directory containing CSV files (default: csv)')
    parser.add_argument('--no-grid', action='store_true',
                       help='Disable grid on plots')

    args = parser.parse_args()

    try:
        plotter = CSVPlotter(csv_dir=args.csv_dir)
    except FileNotFoundError as e:
        print(f"Error: {e}", file=sys.stderr)
        sys.exit(1)

    try:
        if args.list:
            plotter.list_available()

        elif args.plot:
            if len(args.plot) == 1:
                plotter.plot_single(args.plot[0],
                                  output_file=args.output,
                                  grid=not args.no_grid)
            else:
                plotter.plot_multiple(args.plot,
                                    output_file=args.output,
                                    grid=not args.no_grid)

        elif args.category:
            plotter.plot_by_category(args.category,
                                   output_file=args.output)

        elif args.all:
            plotter.plot_all()

        else:
            parser.print_help()

    except Exception as e:
        print(f"Error: {e}", file=sys.stderr)
        sys.exit(1)


if __name__ == '__main__':
    main()
