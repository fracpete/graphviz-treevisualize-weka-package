How to make a release
=====================

Preparation
-----------

* Change the artifact ID in `pom.xml` to today's date, e.g.:

  ```
  2017.3.3-SNAPSHOT
  ```

* Update the version and date in `Description.props` to reflect new version:

  ```
  Version=2017.3.3
  Date=2017-03-03
  ```

Weka package
------------

* Commit/push all changes

* For both, 3.8 and 3.9, do:

    * Update Weka version in `Depends` and `PackageURL` suffix (`-3.8.x` or `-3.9.x`):
     
      ```
      PackageURL=https://github.com/fracpete/graphviz-treevisualize-weka-package/releases/download/v2017.3.3/graphviz-treevisualize-2017.3.3-3.8.x.zip
      Depends=weka (>=3.8.1)
      ```
     
      ```
      PackageURL=https://github.com/fracpete/graphviz-treevisualize-weka-package/releases/download/v2017.3.3/graphviz-treevisualize-2017.3.3-3.9.x.zip
      Depends=weka (>=3.9.1)
      ```
    
    * Run the following command to generate the package archive for version `2017.3.3`:
    
      ```
      ant -f build_package.xml -Dpackage=graphviz-treevisualize-2017.3.3 clean make_package
      ```
      
    * Collect `.zip` file from `dist` directory and add appropriate suffix (`-3.8.x` or `-3.9.x`)

* Create a release tag on github (v2017.3.3)
* Add release notes
* Upload collected package archives for 3.8.x and 3.9.x
* Update download link in README.md


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

