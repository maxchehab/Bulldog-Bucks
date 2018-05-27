import 'package:flutter/material.dart';

enum HomeDropDown { settings, share }

class PopupMenu extends StatelessWidget {
  final EdgeInsets padding;
  final PopupMenuItemSelected<HomeDropDown> onSelected;
  PopupMenu({this.padding, this.onSelected});
  @override
  Widget build(BuildContext context) {
    return (new Container(
      padding: padding,
      alignment: FractionalOffset.center,
      child: new PopupMenuButton<HomeDropDown>(
        padding: new EdgeInsets.symmetric(vertical: 0.0),
        onSelected: this.onSelected,
        icon: new Icon(
          Icons.more_vert,
          color: Colors.white70,
        ),
        itemBuilder: (BuildContext context) => <PopupMenuEntry<HomeDropDown>>[
              const PopupMenuItem<HomeDropDown>(
                value: HomeDropDown.settings,
                child: const Text('Settings'),
              ),
              const PopupMenuItem<HomeDropDown>(
                value: HomeDropDown.share,
                child: const Text('Share'),
              )
            ],
      ),
    ));
  }
}
