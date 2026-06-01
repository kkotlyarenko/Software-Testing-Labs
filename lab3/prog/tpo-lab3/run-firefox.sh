#!/usr/bin/env bash
set -euo pipefail
./gradlew test -Dbrowser=firefox --rerun-tasks "$@"
