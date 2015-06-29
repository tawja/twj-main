#!/bin/bash -v

# NODE_MODULES
rm -rf node_modules/
npm install --ignore-scripts
npm dedupe
#npm shrinkwrap
#tar cfh node_modules.tar node_modules/
tar cf node_modules.tar node_modules/
#mv node_modules.tar ../src/main/webapp/frontend/
rm -rf node_modules/

# BOWER_COMPONENTS
rm -rf bower_components/
bower install --ignore-scripts --allow-root
#tar cfh bower_components.tar bower_components/
tar cf bower_components.tar bower_components/
#mv bower_components.tar ../src/main/webapp/frontend/
rm -rf bower_components/

# Global Instals
#npm install -g grunt-cli
#npm install -g npm-check-updates

## Check latest versions for mu packages
# npm-check-updates
# SAVE package.json
# npm-check-updates -u

## Check latest versions for all packages
# npm outdated

## Replace package.json by package-wildcard.json
#npm update --save
