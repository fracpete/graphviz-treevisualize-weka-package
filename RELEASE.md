How to make a release
=====================

* Run the following command to generate the package archive for version `1.0.0`:

  <pre>
  ant -f build_package.xml -Dpackage=graphviz-treevisualize-1.0.0 clean make_package
  </pre>

* Create a release tag on github (v1.0.0)
* add release notes
* upload package archive from `dist`

