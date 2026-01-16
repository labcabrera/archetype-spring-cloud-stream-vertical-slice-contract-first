#!/usr/bin/env bash
set -euo pipefail

usage() {
	cat <<EOF
Usage: $0 <version>

Create and finish a git-flow release using 'develop' as source and
merge the release into 'main'. The script will push the resulting
main branch and the created tag to origin.

Example:
	$0 0.1.0

Requirements:
	- git
	- git-flow (AVH git-flow or equivalent)
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

if ! command -v git >/dev/null 2>&1; then
	echo "git not found in PATH" >&2
	exit 4
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

# Update gradle.properties on the release branch so the release contains the bumped version
REPO_ROOT=$(git rev-parse --show-toplevel)
GRADLE_PROPS_FILE="$REPO_ROOT/gradle.properties"
echo "Updating $GRADLE_PROPS_FILE with version $VERSION"
if [ -f "$GRADLE_PROPS_FILE" ]; then
	if grep -qE '^version\s*=' "$GRADLE_PROPS_FILE"; then
		sed -i -E "s/^version\s*=.*/version=${VERSION}/" "$GRADLE_PROPS_FILE"
	else
		echo "version=${VERSION}" >> "$GRADLE_PROPS_FILE"
	fi
else
	echo "version=${VERSION}" > "$GRADLE_PROPS_FILE"
fi

# Commit the change only if there are modifications
if git diff --quiet --exit-code -- "$GRADLE_PROPS_FILE"; then
	echo "No changes to commit in $GRADLE_PROPS_FILE"
else
	git add "$GRADLE_PROPS_FILE"
	git commit -m "Bump version to $VERSION"
fi

RELEASE_BRANCH="release/$VERSION"
echo "Pushing release branch to origin: $RELEASE_BRANCH"
git push -u origin "$RELEASE_BRANCH"

echo "Finishing git-flow release: $VERSION (this will merge into 'main' and 'develop' and create tag '$VERSION')"
# Finish the release; suppress prompts if git-flow supports non-interactive finish
git flow release finish "$VERSION"

echo "Pushing 'main' branch to origin..."
git push origin main

echo "Pushing 'develop' branch to origin..."
git push origin develop

echo "Pushing tag '$VERSION' to origin..."
git push origin "refs/tags/$VERSION"

cat <<EOF
Done. Release '$VERSION' finished, 'main' and 'develop' pushed, and tag '$VERSION' pushed to origin.

If your workflow uses pull requests instead of finishing locally, consider
starting the release branch and opening a PR from '$RELEASE_BRANCH' into 'main' instead.
EOF

exit 0

