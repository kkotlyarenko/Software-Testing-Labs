#!/usr/bin/env bash
set -euo pipefail

./gradlew test -Dbrowser=chrome --rerun-tasks "$@" 2>&1 | sed 's/^/[chrome] /' &
PID_CHROME=$!

./gradlew test -Dbrowser=firefox "$@" --rerun-tasks 2>&1 | sed 's/^/[firefox] /' &
PID_FIREFOX=$!

FAIL=0
wait $PID_CHROME  || FAIL=$((FAIL + 1))
wait $PID_FIREFOX || FAIL=$((FAIL + 1))

if [ $FAIL -gt 0 ]; then
    echo ""
    echo "ERROR: $FAIL browser run(s) failed."
    exit 1
fi

echo ""
echo "All browser runs passed."
