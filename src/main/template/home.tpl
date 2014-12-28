<!DOCTYPE html>
<html lang="en" ng-app="myApp" class="no-js">
<head>
  <meta charset="utf-8">
  <title>Whichclasses.com</title>
  <meta name="description" content="">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="/static/app.css">
</head>
<body>
  <ul class="menu">
    <li><a href="#/view1">view1</a></li>
    <li><a href="#/view2">view2</a></li>
  </ul>

  <div ng-view></div>

  <script src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.3.8/angular.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.3.8/angular-route.min.js"></script>
  <script src="/static/app.js"></script>
  <script src="/static/view1/view1.js"></script>
  <script src="/static/view2/view2.js"></script>
</body>
</html>
