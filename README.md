# gradle-dokka-pages

This plugin creates a task `dokkaPages` that generates a landing page for all modules with a dokka plugin applied.
The landing page is a simple HTML page that links to the generated documentation.

This plugin is intended to be used with multi-module projects.

### Output Format
The landing page and the generated documentation are stored in the `build/dokkaPages` directory. Modules with the 
`dockka` plugin will generate KDocs and modules with the `dokka-javadoc` plugin will generate Javadocs.

The following is an example output where module1 has both KDocs and Javadocs and module2 only has Javadocs:

```
./build/dokkaPages/
├── module1/
│   ├── html/
│   │   └── {KDocs}
│   └── javadoc/
│       └── {Javadocs}
├── module2/
│   └── html/
│       └── {Kdocs}
└── index.html
```

When publishing to GitHub Pages, publish the `build/dokkaPages` directory. This will publish the landing page and the 
generated documentation.
