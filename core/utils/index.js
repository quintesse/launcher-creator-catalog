'use strict';

const path = require('path');

function log(res, ...args) {
    console.log(...args);
    return res;
}

function moduleInfo(module) {
    const pkg = path.join(path.dirname(module.filename), 'package.json');
    return require(pkg);
}

function moduleName(module) {
    return path.basename(moduleInfo(module).name);
}

exports.moduleInfo = moduleInfo;
exports.moduleName = moduleName;
