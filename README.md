# GeoStereo
Android Application that plays music through a Spotify API based on a user's current location found through Google Maps API and also checking Google Weather API.

General Flow:
1. User opens application after installation and giving location permissions.
2. Hit Begin Session button to begin listening session.
3. Application sends API request to Google maps for any potential new locations, also checks its data to see if the user is already close to a prior available
   location to avoid more API calls.
4. On pulling an accurate result, application sends a request to a Spotify API for a song related to a nearby location. If no accurate reults nearby, the
   application requests from Google Weather API to determine what the location's weather is and finds an appropriate song based off Weather result.
5. Application sends request to Spotify for a specific playlist already containing songs which belong within specific categories.

Other features to implement might be factoring in local Date time for special days such as Halloween or days leading up to Christmas for thematical playlists.
