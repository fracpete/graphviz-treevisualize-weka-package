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

Additional options can be specified with the `AdditionalOptions` property in
the same properties file.

