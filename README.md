# Eternal Winter Reborn

It snows in every biome. Deserts, jungles and plains get snowfall instead of rain or nothing, water freezes over and snow settles on the ground. Rivers and oceans are left alone by default so you can still travel by boat.

Port of [Eternal Winter](https://github.com/ichttt/Eternal-Winter) by ichttt to Minecraft 1.21.1, for NeoForge and Fabric.

## Config

`config/eternalwinter.json`:

- `listMode`: `BLACKLIST` changes every biome except the ones in `biomeList`; `WHITELIST` changes only those.
- `biomeList`: biome ids, with or without the namespace.
- `downfall`: 0 to 1 to set every changed biome's downfall, or -1 to keep each biome's own.

## Building

`./gradlew buildAll` builds both loaders and runs the game tests.

## License

GPL-3.0, same as the original. See [LICENSE](LICENSE).
