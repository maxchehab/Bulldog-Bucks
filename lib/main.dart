import 'package:flutter/material.dart';
import 'package:bulldog_bucks/Screens/Login/index.dart';
import 'package:bulldog_bucks/Screens/Home/index.dart';

void main() {
  runApp(new MaterialApp(
    home: new LoginScreen(),
    routes: <String, WidgetBuilder>{
      '/login': (BuildContext context) => new LoginScreen(),
      '/home': (BuildContext context) => new HomeScreen()
    },
  ));
}
