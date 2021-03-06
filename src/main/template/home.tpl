<!DOCTYPE html>
<html lang="en" ng-app="whichclasses" class="no-js">
<head>
  <!-- <base href="/"> -->
  <meta charset="utf-8">
  <title>Whichclasses.com</title>
  <meta name="description" content="">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="/static/app.css">
  <link rel="icon" href="data:;base64,iVBORw0KGgo=">
</head>
<body>
  <ul class="menu">
    <li><a href="/">home</a></li>
    <li><a href="/nowhere">not-implemented</a></li>
  </ul>

  <div ng-view></div>

  <!-- TODO: look into generating a minified bundle for prod and replacing inline. -->
  <!-- TODO: look into HTTP/2 "server push" these -->
  <script src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.3.8/angular.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.3.8/angular-route.js"></script>
  <script src="/static/components/tce_data_service.js"></script>
  <script src="/static/home/home.js"></script>
  <script src="/static/search/search_ctrl.js"></script>
  <script src="/static/app.js"></script>

  <!-- TODO: look into sending an initial data load inline.
       Should be easy-ish to leverage TceDataService for this. -->
</body>
</html>
