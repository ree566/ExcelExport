/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var path = require('path');

module.exports = function (grunt) {
    // Project configuration.
    grunt.initConfig({
        bower: {
            install: {
                options: {
                    copy: false,
                    targetDir: './lib',
                    layout: 'byType',
                    install: true,
                    verbose: false,
                    prune: false,
                    cleanTargetDir: false,
                    cleanBowerDir: false,
                    bowerOptions: {}
                }
            }
        },
        dist: {
            files: {
                'dist/<%= pkg.name %>.min.js': ['<%= concat.dist.dest %>']
            }
        }
    });

    grunt.loadNpmTasks('grunt-bower-task');
    grunt.loadNpmTasks('grunt-dist');
};
