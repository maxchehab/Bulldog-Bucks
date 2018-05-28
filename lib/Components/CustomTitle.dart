import 'package:flutter/material.dart';

class CustomTitle extends StatelessWidget {
  final String title;
  final double width;
  final double height;
  CustomTitle(String title, {this.width, this.height}) : this.title = title;
  @override
  Widget build(BuildContext context) {
    return (new Container(
        width: width,
        height: height,
        child: new Center(
            child: new RichText(
          textAlign: TextAlign.center,
          text: new TextSpan(
              text: title,
              style:
                  new TextStyle(fontSize: 38.0, fontWeight: FontWeight.bold)),
        ))));
  }
}
