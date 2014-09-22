graphviz-treevisualize-weka-package
===================================

TreeVisualize plugin for the Explorer using GraphViz to generate the tree.

By default, the plugin expects the `dot` executable to be available on the
system path. If that should not be the case, you can specify the correct
location in the `GraphVizTreeVisualization.props` properties file.

Simply create the following file:

* Linux/Mac
  <pre>
  $HOME/wekafiles/GraphVizTreeVisualization.props
  </pre>

* Windows
  <pre>
  %USERPROFILE%\wekafiles\GraphVizTreeVisualization.props
  </pre>

And add the following content (adjust path, of course):

* Linux/Mac
  <pre>
  Executable=/some/where/dot
  </pre>

* Windows
  <pre>
  Executable=C:/Progam Files/some/where/dot.exe
  </pre>

Additional options for the executable can be specified with the
`AdditionalOptions` property in the same properties file.


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
      <version>2014.8.1</version>
      <type>jar</type>
      <exclusions>
        <exclusion>
          <groupId>nz.ac.waikato.cms.weka</groupId>
          <artifactId>weka-dev</artifactId>
        </exclusion>
      </exclusions>
    </dependency>
```

