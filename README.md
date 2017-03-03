graphviz-treevisualize-weka-package
===================================

TreeVisualize plugin for the Explorer using GraphViz (http://www.graphviz.org/)
to generate the tree.

By default, the plugin expects the `dot` executable to be available on the
system path. If that should not be the case, you can specify the correct
location in the `GraphVizTreeVisualization.props` properties file.

Simply create the following file:

* Linux/Mac
  ```
  $HOME/wekafiles/props/GraphVizTreeVisualization.props
  ```
`
* Windows
  ```
  %USERPROFILE%\wekafiles\props\GraphVizTreeVisualization.props
  ```

And add the following content (adjust path, of course):

* Linux/Mac
  ```
  Executable=/some/where/dot
  ```

* Windows
  ```
  Executable=C:/Progam Files/some/where/dot.exe
  ```

Additional options for the executable can be specified with the
`AdditionalOptions` property in the same properties file.


GraphVizTreeVisualization.props
-------------------------------

Explanation of parameters:

* `Executable` -- the path to the executable, if not on the system path 
* `AdditionalOptions` -- additional options to use, like output format
* `ImageExtension` -- the image extension to use
* `Debug` -- whether to output debugging information
* `CleanUpTempFiles` -- whether to clean up temporary files
* `DialogWidth` -- the default width of the dialog displaying the graph
* `DialogHeight` -- the default height of the dialog displaying the graph


Releases
--------

* [2017.3.3](https://github.com/fracpete/graphviz-treevisualize-weka-package/releases/download/v2017.3.3/graphviz-treevisualize-2017.3.3.zip) (Weka >= 3.9.1)
* [2014.8.1](https://github.com/fracpete/graphviz-treevisualize-weka-package/releases/download/v2014.8.1/graphviz-treevisualize-2014.8.1.zip) (Weka >= 3.7.11 and < 3.8.1 and < 3.9.1)


How to use packages
-------------------

For more information on how to install the package, see:

http://weka.wikispaces.com/How+do+I+use+the+package+manager%3F


Maven
-----

Add the following dependency in your `pom.xml` to include the package:

```xml
    <dependency>
      <groupId>com.github.fracpete</groupId>
      <artifactId>graphviz-treevisualize-weka-package</artifactId>
      <version>2017.3.3</version>
      <type>jar</type>
      <exclusions>
        <exclusion>
          <groupId>nz.ac.waikato.cms.weka</groupId>
          <artifactId>weka-dev</artifactId>
        </exclusion>
      </exclusions>
    </dependency>
```

