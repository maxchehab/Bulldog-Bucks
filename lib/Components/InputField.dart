import 'package:flutter/material.dart';

class InputField extends StatefulWidget {
  final String hint;
  final bool obscure;
  final IconData icon;
  final ValueChanged<String> onFieldCompleted;
  final bool autofocus;
  final FocusNode focusNode;
  final TextInputType keyboardType;
  final TextEditingController controller;
  InputField(
      {this.hint,
      this.obscure,
      this.icon,
      this.onFieldCompleted,
      this.controller,
      this.autofocus = false,
      this.focusNode,
      this.keyboardType});

  @override
  InputFieldState createState() => new InputFieldState();
}

class InputFieldState extends State<InputField> {
  bool showText = false;

  @override
  Widget build(BuildContext context) {
    return (new Container(
        decoration: new BoxDecoration(
          border: new Border(
            bottom: new BorderSide(
              width: 0.5,
              color: Colors.white24,
            ),
          ),
        ),
        child: new Stack(alignment: Alignment.bottomRight, children: [
          new TextFormField(
            controller: widget.controller,
            keyboardType: widget.keyboardType,
            focusNode: widget.focusNode,
            autofocus: widget.autofocus,
            onFieldSubmitted: (value) {
              widget.onFieldCompleted(value);
            },
            obscureText: widget.obscure && !showText,
            style: const TextStyle(
              color: Colors.white,
            ),
            decoration: new InputDecoration(
              icon: new Icon(
                widget.icon,
                color: Colors.white,
              ),
              border: InputBorder.none,
              hintText: widget.hint,
              hintStyle: const TextStyle(color: Colors.white, fontSize: 15.0),
              contentPadding: widget.obscure
                  ? const EdgeInsets.only(
                      top: 30.0, right: 100.0, bottom: 30.0, left: 5.0)
                  : const EdgeInsets.only(
                      top: 30.0, right: 30.0, bottom: 30.0, left: 5.0),
            ),
          ),
          widget.obscure
              ? new Padding(
                  padding: new EdgeInsets.all(30.0),
                  child: new InkWell(
                      onTap: () {
                        setState(() {
                          showText = !showText;
                        });
                      },
                      child: new Text(showText ? "Hide" : "Show",
                          style: const TextStyle(
                            color: Colors.white,
                          ))))
              : new Container(),
        ])));
  }
}
