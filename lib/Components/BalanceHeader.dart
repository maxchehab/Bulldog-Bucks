import 'package:flutter/material.dart';
import 'Carousel.dart';

class BalanceHeader extends StatelessWidget {
  final DecorationImage backgroundImage;
  final String balance;
  final Animation<double> containerGrowAnimation;
  final double height = 100.0;
  BalanceHeader(
      {this.backgroundImage, this.containerGrowAnimation, this.balance});

  @override
  Widget build(BuildContext context) {
    final Orientation orientation = MediaQuery.of(context).orientation;
    bool isLandscape = orientation == Orientation.landscape;
    Size screenSize = MediaQuery.of(context).size;
    double height = screenSize.height / 2.5;
    List<Widget> children = [
      new Padding(
          padding: new EdgeInsets.only(top: 32.0),
          child: new Text(
            "Good Morning!",
            style: new TextStyle(
                fontSize: 30.0,
                letterSpacing: 1.2,
                fontWeight: FontWeight.w300,
                color: Colors.white),
          )),
      new Container(
        width: screenSize.width,
        child: (new Carousel(height: 150.0, children: [
          new Column(children: <Widget>[
            new Text(this.balance,
                style: new TextStyle(fontSize: 64.0, color: Colors.white)),
            new Text("You have \$12.00 to spend today.",
                style: new TextStyle(
                    fontSize: 18.0,
                    color: Colors.white,
                    fontWeight: FontWeight.w300))
          ]),
          new Column(children: <Widget>[
            new Text("45 swipes",
                style: new TextStyle(fontSize: 64.0, color: Colors.white)),
            new Text("You have 3 swipes to spend today.",
                style: new TextStyle(
                    fontSize: 18.0,
                    color: Colors.white,
                    fontWeight: FontWeight.w300))
          ])
        ])),
      ),
    ];

    return (new Container(
        width: screenSize.width,
        height: height,
        decoration: new BoxDecoration(image: backgroundImage),
        child: new Container(
          decoration: new BoxDecoration(
              gradient: new LinearGradient(
            colors: <Color>[
              const Color.fromRGBO(110, 101, 103, 0.6),
              const Color.fromRGBO(51, 51, 63, 0.9),
            ],
            stops: [0.2, 1.0],
            begin: const FractionalOffset(0.0, 0.0),
            end: const FractionalOffset(0.0, 1.0),
          )),
          child: isLandscape
              ? new ListView(
                  children: <Widget>[
                    new Flex(
                      direction: Axis.vertical,
                      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                      children: children,
                    )
                  ],
                )
              : new Column(
                  mainAxisAlignment: MainAxisAlignment.spaceAround,
                  children: children,
                ),
        )));
  }
}
