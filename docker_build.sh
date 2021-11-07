export DOCKER_IMAGE_VERSION=`cat version.txt`
docker build -t $DOCKER_IMAGE_INSTANCE/$DOCKER_IMAGE_NAME:$DOCKER_IMAGE_VERSION . --build-arg VERSION="$VERSION"