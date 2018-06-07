import 'package:flutter/material.dart';

class Location {
  final List<String> keywords;
  final DecorationImage image;
  final String name;

  Location({this.keywords, String imagePath, this.name})
      : this.image = new DecorationImage(
          image: new ExactAssetImage(imagePath),
          fit: BoxFit.fitWidth,
        );

  static List<Location> locations = [
    new Location(
        keywords: ["connection"],
        imagePath: "assets/logos/sub-connection-logo.png",
        name: "Sub Connection"),
    new Location(
        keywords: ["bulldog"],
        imagePath: "assets/logos/the-bulldog-logo.png",
        name: "The Bulldog"),
    new Location(
        keywords: ["financial"],
        imagePath: "assets/logos/gonzaga-logo.png",
        name: "Financial Office"),
    new Location(
        keywords: ["follett"],
        imagePath: "assets/logos/gonzaga-logo.png",
        name: "Follett"),
    new Location(
        keywords: ["cog"],
        imagePath: "assets/logos/sodexo-logo.jpg",
        name: "The Cog"),
    new Location(
        keywords: ["wagon"],
        imagePath: "assets/logos/zaggin-wagon-logo.png",
        name: "The Zaggin' Wagon"),
    new Location(
        keywords: ["duffs"],
        imagePath: "assets/logos/duffs-logo.png",
        name: "Duff's Bistro"),
    new Location(
        keywords: ["papercut"],
        imagePath: "assets/logos/papercut-logo.png",
        name: "Papercut Printing"),
    new Location(
        keywords: ["einsteins"],
        imagePath: "assets/logos/einstein-bros-logo.gif",
        name: "Einstein Bagel Bros"),
    new Location(
        keywords: ["vending"],
        imagePath: "assets/logos/sodexo-logo.jpg",
        name: "Sodexo Vending"),
    new Location(
        keywords: ["marketplace"],
        imagePath: "assets/logos/sodexo-logo.jpg",
        name: "The Marketplace"),
    new Location(
        keywords: ["aloha", "island", "grill"],
        imagePath: "assets/logos/aloha-island-grill-logo.png",
        name: "Aloha Island Grill"),
    new Location(
        keywords: ["bruchis"],
        imagePath: "assets/logos/bruchis-logo.png",
        name: "Bruchi's Cheesesteaks & Subs"),
    new Location(
        keywords: ["carls", "jr"],
        imagePath: "assets/logos/carls-jr-logo.png",
        name: "Carl's Jr."),
    new Location(
        keywords: ["carusos"],
        imagePath: "assets/logos/carusos-logo.png",
        name: "Caruso's Sandwiches and Artisan Pizza"),
    new Location(
        keywords: ["clarks", "fork"],
        imagePath: "assets/logos/clarks-fork-logo.jpg",
        name: "Clark's Fork"),
    new Location(
        keywords: ["dominos"],
        imagePath: "assets/logos/dominos-logo.png",
        name: "Domino's Pizza"),
    new Location(
        keywords: ["dutch"],
        imagePath: "assets/logos/dutch-bros-coffee-logo.jpg",
        name: "Dutch Bros Coffee"),
    new Location(
        keywords: ["forza"],
        imagePath: "assets/logos/forza-logo.png",
        name: "Forza Coffee Company"),
    new Location(
        keywords: ["froyo", "earth", "froyoearth"],
        imagePath: "assets/logos/froyo-earth-logo.png",
        name: "Froyo Earth"),
    new Location(
        keywords: ["jimmy"],
        imagePath: "assets/logos/jimmy-johns-logo.png",
        name: "Jimmy John's"),
    new Location(
        keywords: ["kalico", "kitchen"],
        imagePath: "assets/logos/kalico-kitchen-logo.jpg",
        name: "Kalico Kitchen"),
    new Location(
        keywords: ["mcdonalds"],
        imagePath: "assets/logos/mcdonalds-logo.jpeg",
        name: "McDonalds"),
    new Location(
        keywords: ["method", "juice"],
        imagePath: "assets/logos/method-juice-logo.jpeg",
        name: "Method Juice Cafe"),
    new Location(
        keywords: ["mod"],
        imagePath: "assets/logos/mod-pizza-logo.png",
        name: "Mod Pizza"),
    new Location(
        keywords: ["papa"],
        imagePath: "assets/logos/papa-johns-logo.jpeg",
        name: "Papa John's Pizza"),
    new Location(
        keywords: ["pita", "pit"],
        imagePath: "assets/logos/pita-pit-logo.jpeg",
        name: "Pita Pit"),
    new Location(
        keywords: ["qdoba"],
        imagePath: "assets/logos/qdoba-logo.png",
        name: "Qdoba"),
    new Location(
        keywords: ["sonic"],
        imagePath: "assets/logos/sonic-logo.jpg",
        name: "Sonic"),
    new Location(
        keywords: ["starbucks"],
        imagePath: "assets/logos/starbucks-logo.png",
        name: "Starbucks"),
    new Location(
        keywords: ["sushi", "sakai"],
        imagePath: "assets/logos/sushi-sakai-logo.png",
        name: "Sushi Sakai"),
    new Location(
        keywords: ["sweeto", "burrito"],
        imagePath: "assets/logos/sweeto-burrito-logo.jpg",
        name: "Sweeto Burrito"),
    new Location(
        keywords: ["thomas", "hammer"],
        imagePath: "assets/logos/thomas-hammer-logo.png",
        name: "Thomas Hammer Coffee Roasters"),
    new Location(
        keywords: ["ultimate", "bagel"],
        imagePath: "assets/logos/ultimate-bagel-logo.jpeg",
        name: "Ultimate Bagel Inc."),
    new Location(
        keywords: ["urm"],
        imagePath: "assets/logos/urm-logo.png",
        name: "URM Cash & Carry"),
    new Location(
        keywords: ["wendys"],
        imagePath: "assets/logos/wendys-logo.png",
        name: "Wendy's"),
    new Location(
        keywords: ["zag", "shop"],
        imagePath: "assets/logos/zag-shop-logo.jpg",
        name: "The Zag Shop"),
  ];

  static Location findLocationFromKeyWords(String keywords) {
    keywords = keywords.replaceAll(new RegExp(r"[^A-Za-z\s]"), "");
    List<String> keys = keywords.toLowerCase().split(' ');

    for (Location location in locations) {
      for (String key in keys) {
        key = key.toLowerCase();
        if (location.keywords.contains(key)) {
          return location;
        }
      }
    }

    /// TODO report keys that are not found
    return new Location(
        name: keywords, imagePath: "assets/logos/sodexo-logo.jpg");
  }
}
