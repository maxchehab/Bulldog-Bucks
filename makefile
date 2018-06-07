all:
	flutter run

emulator:
	emulator -avd Pixel_2_API_26 &

install:
	flutter build apk && flutter install -d "Pixel 2"