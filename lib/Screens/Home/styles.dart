import 'package:flutter/material.dart';
import '../../Components/Location.dart';

DecorationImage backgroundImage = new DecorationImage(
  image: new ExactAssetImage('assets/home.jpeg'),
  fit: BoxFit.cover,
);

DecorationImage logoImage = new DecorationImage(
  image: new ExactAssetImage('assets/logo.png'),
  fit: BoxFit.cover,
);

List<Location> locations = [
  new Location(
      ["aloha", "island", "grill"], "assets/logos/aloha-island-grill-logo.png"),
  new Location(["bruchis"], "assets/logos/bruchis-logo.png"),
  new Location(["carls", "jr"], "assets/logos/carls-jr-logo.png"),
  new Location(["carusos"], "assets/logos/carusos-logo.png"),
  new Location(["clarks", "fork"], "assets/logos/clarks-fork-logo.jpg"),
  new Location(["dominos"], "assets/logos/dominos-logo.png"),
  new Location(
      ["dutch", "bros", "coffee"], "assets/logos/dutch-bros-coffee-logo.jpg"),
  new Location(["forza"], "assets/logos/forza-logo.png"),
  new Location(["froyo", "earth"], "assets/logos/froyo-earth-logo.png"),
  new Location(["jimmy"], "assets/logos/jimmy-johns-logo.png"),
  new Location(["kalico", "kitchen"], "assets/logos/kalico-kitchen-logo.jpg"),
  new Location(["mcdonalds"], "assets/logos/mcdonalds-logo.jpeg"),
  new Location(["method", "juice"], "assets/logos/method-juice-logo.jpeg"),
  new Location(["mod", "pizza"], "assets/logos/mod-pizza-logo.png"),
  new Location(["papa"], "assets/logos/papa-johns-logo.jpeg"),
  new Location(["pita", "pit"], "assets/logos/pita-pit-logo.jpeg"),
  new Location(["qdoba"], "assets/logos/qdoba-logo.png"),
  new Location(["sonic"], "assets/logos/sonic-logo.jpg"),
  new Location(["starbucks"], "assets/logos/starbucks-logo.png"),
  new Location(["sushi", "sakai"], "assets/logos/sushi-sakai-logo.png"),
  new Location(["sweeto", "burrito"], "assets/logos/sweeto-burrito-logo.jpg"),
  new Location(["thomas", "hammer"], "assets/logos/thomas-hammer-logo.png"),
  new Location(["ultimate", "bagel"], "assets/logos/ultimate-bagel-logo.jpeg"),
  new Location(["urm"], "assets/logos/urm-logo.png"),
  new Location(["wendys"], "assets/logos/wendys-logo.png"),
  new Location(["zag", "shop"], "assets/logos/zag-shop-logo.jpg"),
];
