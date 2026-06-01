#!/usr/bin/env bash
set -euo pipefail
./gradlew test -Dbrowser=chrome --rerun-tasks "$@"
