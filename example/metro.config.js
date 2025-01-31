const path = require('path');

module.exports = {
  resolver: {
    blockList: [
      new RegExp(path.resolve('..', 'node_modules', 'react')),
      new RegExp(path.resolve('..', 'node_modules', 'react-native'))
    ],
    nodeModulesPaths: [
      path.resolve(__dirname, './node_modules'),
      path.resolve(__dirname, '../node_modules')
    ],
    extraNodeModules: {
      'react-native-vlcplayer-view-tv': '..'
    }
  },
  watchFolders: [path.resolve(__dirname, '..')],
  transformer: {
    getTransformOptions: async () => ({
      transform: {
        experimentalImportSupport: false,
        inlineRequires: true
      }
    })
  }
};
