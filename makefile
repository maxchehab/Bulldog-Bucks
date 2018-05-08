all:
	flutter run

emulator:
	emulator -list-avds | xargs -i emulator -avd {} &