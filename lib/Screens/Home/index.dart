import 'package:flutter/material.dart';
import '../../Components/AddButton.dart';
import '../../Components/Settings.dart';
import 'styles.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/animation.dart';
import 'dart:async';
import '../../Components/TransactionContainer.dart';
import '../../Components/BalanceHeader.dart';
import '../../Components/FadeBox.dart';
import '../../Components/PopupMenu.dart';
import 'package:flutter/scheduler.dart' show timeDilation;

class HomeScreen extends StatefulWidget {
  const HomeScreen({Key key}) : super(key: key);

  @override
  HomeScreenState createState() => new HomeScreenState();
}

class HomeScreenState extends State<HomeScreen> with TickerProviderStateMixin {
  Animation<double> containerGrowAnimation;
  AnimationController _screenController;
  AnimationController _settingController;
  Animation<double> buttonGrowAnimation;
  Animation<double> listTileWidth;
  Animation<Alignment> listSlideAnimation;
  Animation<Alignment> buttonSwingAnimation;
  Animation<EdgeInsets> listSlidePosition;
  Animation<Color> fadeScreenAnimation;
  bool showSettings = false;

  var animateStatus = 0;

  @override
  void initState() {
    super.initState();

    _screenController = new AnimationController(
        duration: new Duration(milliseconds: 2000), vsync: this);
    _settingController = new AnimationController(
        duration: new Duration(milliseconds: 250), vsync: this);

    fadeScreenAnimation = new ColorTween(
      begin: const Color.fromRGBO(247, 64, 106, 1.0),
      end: const Color.fromRGBO(247, 64, 106, 0.0),
    ).animate(
      new CurvedAnimation(
        parent: _screenController,
        curve: Curves.ease,
      ),
    );
    containerGrowAnimation = new CurvedAnimation(
      parent: _screenController,
      curve: Curves.easeIn,
    );

    buttonGrowAnimation = new CurvedAnimation(
      parent: _screenController,
      curve: Curves.easeOut,
    );
    containerGrowAnimation.addListener(() {
      this.setState(() {});
    });
    containerGrowAnimation.addStatusListener((AnimationStatus status) {});

    listTileWidth = new Tween<double>(
      begin: 1000.0,
      end: 600.0,
    ).animate(
      new CurvedAnimation(
        parent: _screenController,
        curve: new Interval(
          0.225,
          0.600,
          curve: Curves.bounceIn,
        ),
      ),
    );

    listSlideAnimation = new AlignmentTween(
      begin: Alignment.topCenter,
      end: Alignment.bottomCenter,
    ).animate(
      new CurvedAnimation(
        parent: _screenController,
        curve: new Interval(
          0.325,
          0.700,
          curve: Curves.ease,
        ),
      ),
    );
    buttonSwingAnimation = new AlignmentTween(
      begin: Alignment.topCenter,
      end: Alignment.bottomRight,
    ).animate(
      new CurvedAnimation(
        parent: _screenController,
        curve: new Interval(
          0.225,
          0.600,
          curve: Curves.ease,
        ),
      ),
    );
    listSlidePosition = new EdgeInsetsTween(
      begin: const EdgeInsets.only(bottom: 16.0),
      end: const EdgeInsets.only(bottom: 80.0),
    ).animate(
      new CurvedAnimation(
        parent: _screenController,
        curve: new Interval(
          0.325,
          0.800,
          curve: Curves.ease,
        ),
      ),
    );
    _screenController.forward();
  }

  @override
  void dispose() {
    _screenController.dispose();
    _settingController.dispose();
    super.dispose();
  }

  Future<Null> _showSettings() async {
    try {
      await _settingController.forward();
    } on TickerCanceled {}
  }

  Future<Null> _hideSettings(bool dismissed) async {
    debugPrint("dismissed: " + dismissed.toString());
    if (dismissed) {
      setState(() {
        showSettings = false;
      });
      _settingController.reset();
    } else {
      try {
        await _settingController.reverse();
        setState(() {
          showSettings = false;
        });
      } on TickerCanceled {}
    }
  }

  Future<bool> _willPop() {
    if (showSettings) {
      _hideSettings(false);
      return Future<bool>.value(false);
    }
    return Future<bool>.value(true);
  }

  Future<Null> _logout() async {
    Navigator.pushReplacementNamed(context, "/login");
  }

  @override
  Widget build(BuildContext context) {
    timeDilation = 0.3;

    Size screenSize = MediaQuery.of(context).size;
    Stack panel = new Stack(
      alignment: Alignment.bottomRight,
      children: <Widget>[
        new ListView(
          shrinkWrap: _screenController.value < 1 ? false : true,
          padding: const EdgeInsets.all(0.0),
          children: <Widget>[
            new Stack(
              children: <Widget>[
                new BalanceHeader(
                  backgroundImage: backgroundImage,
                  containerGrowAnimation: containerGrowAnimation,
                  balance: "\$100.00",
                ),
                new PopupMenu(
                  onSelected: (HomeDropDown action) {
                    debugPrint("selected: " + action.toString());
                    setState(() {
                      showSettings = true;
                    });
                    _showSettings();
                  },
                  padding: new EdgeInsets.only(
                      left: screenSize.width - 50.0, top: 42.0),
                )
              ],
            ),
            new TransactionContainer(
              listSlideAnimation: listSlideAnimation,
              listSlidePosition: listSlidePosition,
              listTileWidth: listTileWidth,
            )
          ],
        ),
        new FadeBox(
          fadeScreenAnimation: fadeScreenAnimation,
          containerGrowAnimation: containerGrowAnimation,
        ),
        showSettings
            ? new Settings(
                size: screenSize,
                close: (dismissed) => _hideSettings(dismissed),
                controller: _settingController.view,
                logout: _logout,
              )
            : new Container(),
        // new Padding(
        //     padding: new EdgeInsets.all(20.0),
        //     child: new InkWell(
        //         splashColor: Colors.white,
        //         highlightColor: Colors.white,
        //         onTap: () {},
        //         child: new AddButton(
        //           buttonGrowAnimation: buttonGrowAnimation,
        //         )))
      ],
    );
    return new WillPopScope(
        onWillPop: _willPop,
        child: new Scaffold(
          body: new Container(
            decoration: new BoxDecoration(
              color: new Color.fromRGBO(242, 242, 242, 1.0),
            ),
            width: screenSize.width,
            height: screenSize.height,
            child: panel,
          ),
        ));
  }
}
