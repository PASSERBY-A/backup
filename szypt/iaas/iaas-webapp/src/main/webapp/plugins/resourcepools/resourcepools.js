// Licensed to the Apache Software Foundation (ASF) under one
(function (cloudStack) {
  cloudStack.plugins.resourcepools = function(plugin) {
    plugin.ui.addSection({
      id: 'resourcepools',
      title: 'abc',
      preFilter: function(args) {
        return isAdmin();
      },
      show: function() {
        return $('<div>').html('Test plugin section');
      }
    });
  };
}(cloudStack));