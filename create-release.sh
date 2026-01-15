#!/usr/bin/env bash
set -euo pipefail

usage() {
  cat <<EOF
Usage: $0 <version>

Create a git-flow release branch from 'develop' and push it to origin.
This script configures git-flow to use 'main' as the production branch
and 'develop' as the development branch (local git config).

Example:
  $0 1.2.0

Requirements:
  - git
  - git-flow (git-flow AVH or equivalent)
EOF
  exit 1
}

if [ "${#}" -ne 1 ]; then
  usage
fi

VERSION="$1"

if ! git rev-parse --is-inside-work-tree >/dev/null 2>&1; then
  echo "Not inside a git repository." >&2
  exit 2
fi

if [ -n "$(git status --porcelain)" ]; then
  echo "Working tree is not clean. Commit or stash changes before creating a release." >&2
  git status --porcelain
  exit 3
fi

echo "Configuring git-flow to use 'main' and 'develop' (local config)..."
git config gitflow.branch.master main
git config gitflow.branch.develop develop

echo "Fetching origin..."
git fetch --all --prune

echo "Checking out 'develop' and updating from origin/develop..."
git checkout develop
git pull --ff-only origin develop

echo "Starting git-flow release: $VERSION"
git flow release start "$VERSION"

RELEASE_BRANCH="release/$VERSION"
echo "Pushing release branch to origin: $RELEASE_BRANCH"
git push -u origin "$RELEASE_BRANCH"

cat <<EOF
Done. Release branch '$RELEASE_BRANCH' created and pushed.

Next steps (examples):
  - Finish the release locally and merge to 'main':
      git flow release finish "$VERSION"
  - Push the resulting tag and main branch to origin:
      git push origin main --follow-tags

If you prefer to create a PR instead of finishing locally, open a PR from
'$RELEASE_BRANCH' into 'main' and follow your normal release process.
EOF

exit 0
