How to make a release
=====================

Preparation
-----------

* Change the artifact ID in `pom.xml` to today's date, e.g.:

  ```
  2014.12.31-SNAPSHOT
  ```

* Update the version, date and URL in `Description.props` to reflect new
  version, e.g.:

  ```
  Version=2014.12.31
  Date=2014-12-31
  PackageURL=https://github.com/fracpete/graphviz-treevisualize-weka-package/releases/download/v2014.12.31/graphviz-treevisualize-2014.12.31.zip
  ```

Weka package
------------

* Commit/push all changes

* Run the following command to generate the package archive for version `1.0.0`:

  ```
  ant -f build_package.xml -Dpackage=graphviz-treevisualize-1.0.0 clean make_package
  ```

* Create a release tag on github (v1.0.0)
* add release notes
* upload package archive from `dist`


Maven
-----

* Run the following command to deploy the artifact:

  ```
  mvn release:clean release:prepare release:perform
  ```

* log into https://oss.sonatype.org and close/release artifacts

* After successful deployment, push the changes out:

  ```
  git push
  ````

