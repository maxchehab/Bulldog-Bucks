import 'package:flutter/material.dart';

class FreezeSettings extends StatefulWidget {
  @override
  FreezeSettingsState createState() => new FreezeSettingsState();
}

class FreezeSettingsState extends State<FreezeSettings> {
  bool frozen = false;
  @override
  Widget build(BuildContext context) {
    return (new Container(
        padding: new EdgeInsets.all(20.0),
        decoration: new BoxDecoration(
            border: new Border(
                bottom: new BorderSide(
          width: 0.5,
          color: Colors.white24,
        ))),
        child: new SwitchListTile(
          activeColor: Colors.white,
          value: frozen,
          onChanged: (bool value) {
            setState(() {
              frozen = value;
            });
          },
          title: new Text(frozen ? "Unfreeze Zagcard" : "Freeze Zagcard",
              style: new TextStyle(
                  fontSize: 20.0,
                  color: Colors.white,
                  fontWeight: FontWeight.w500)),
          secondary: frozen
              ? const Icon(Icons.lock_outline, color: Colors.white)
              : const Icon(Icons.lock_open, color: Colors.white),
        )));
  }
}
