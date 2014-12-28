<!DOCTYPE html>
<html lang="en" ng-app="whichclasses" class="no-js">
<head>
  <!-- <base href="/"> -->
  <meta charset="utf-8">
  <title>Whichclasses.com</title>
  <meta name="description" content="">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="/static/app.css">
</head>
<body>
  <ul class="menu">
    <li><a href="/">home</a></li>
    <li><a href="/nowhere">not-implemented</a></li>
  </ul>

  <div ng-view></div>

  <!-- TODO: use .min versions for prod -->
  <script src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.3.8/angular.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.3.8/angular-route.js"></script>
  <script src="/static/app.js"></script>
  <script src="/static/home/home.js"></script>
</body>
</html>
