#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

usage() {
  echo "Usage: $0 <load|stress>"
  exit 1
}

TYPE="${1:-}"
case "$TYPE" in
  load)   JMX="$SCRIPT_DIR/jmeter/Load test.jmx" ;;
  stress) JMX="$SCRIPT_DIR/jmeter/Stress test.jmx" ;;
  *)      usage ;;
esac

JTL="$SCRIPT_DIR/$TYPE/results.jtl"
REPORT_DIR="$SCRIPT_DIR/$TYPE/report"

if ! command -v jmeter &>/dev/null; then
  echo "Error: jmeter not found in PATH"
  exit 1
fi

rm -rf "$SCRIPT_DIR/$TYPE"
mkdir -p "$SCRIPT_DIR/$TYPE"

echo "Running $TYPE test..."
jmeter -n \
  -t "$JMX" \
  -l "$JTL" \
  -e -o "$REPORT_DIR"

echo ""
echo "Report: $REPORT_DIR/index.html"

if command -v open &>/dev/null; then
  open "$REPORT_DIR/index.html"
fi
