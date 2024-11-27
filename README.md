<a id="readme-top"></a>
[![MIT License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]

# AndroidCurrencyExchangeWidget

A dynamic and interactive Android home screen widget that provides real-time currency exchange conversions between multiple currencies. It ensures the widget stays up-to-date with the latest conversion rates even when the app is not in use.

### Built With
* [![AndroidStudio][AndroidStudio.com]][AndroidStudio-url]
* [![Kotlin][Kotlin.com]][Kotlin-url]

## About The Project
This is a personal project in the making, I made it for my Italian girlfriend who got confused when she came to Uruguay because of the high numbers displayed in supermarkets (currency exchange problems? cries in: outside of the European Union). She didn't have mobile data, so she needed something that did not require internet. Later we went to Argentina, so I added ARS too, and USD just because I could too.

Here are some pics using it:

![WhatsApp Image 2024-11-27 at 16 07 12_879410c2 cut](https://github.com/user-attachments/assets/91667dbc-88d4-421d-82a5-4542e06f2a90)

The button top right that says "URU" can be tapped to change currency (right now between USD and ARA). 

## Features
*Currency Conversion*:
* Converts values from other currencies including UYU, USD, and ARS to EUR.
* Toggle between target currencies using the widget for quick conversions.

*Interactive Widget*:
* Intuitive number pad for entering amounts directly in the widget.
* Reset and erase buttons for enhanced input control.

*Offline Support*:
* Utilizes WorkManager to fetch real-time conversion rates from an external API at regular intervals when there is an internet connection.
* Automatically retries fetching data when the device regains internet connectivity.
* Stores the latest exchange rates locally using SharedPreferences, ensuring functionality even without an active internet connection.

*Lightweight and Efficient*:
* Minimal impact on performance and battery life.

## Technical Highlights

*WorkManager*:
* Handles background tasks for fetching and storing conversion rates.
* Ensures tasks are retried automatically if they fail due to network issues.

*SharedPreferences*:
* Provides lightweight storage for persisting exchange rates and user settings.

*RemoteViews*:
* Implements a fully functional, dynamic UI for the Android widget.

## Roadmap
- [x] Finish widget with URU-EUR conversion.
- [x] Add USD and ARA conversions.
- [ ] Add option to turn around conversion (Example: change from URU -> EUR to EUR -> URU).
- [ ] Add in-app option to choose conversion currencies available in the widget.
- [ ] Enhance UI with a nicer theme.

*Possible to add in the future*:
Add a server that makes the api call and the app makes the call to my server.

## How to Use

1.  **Clone the Repository**:
    
```bash
git clone https://github.com/yourusername/currency-exchange-widget.git
cd currency-exchange-widget
```

2.  **Setup API Key**:

The app uses the [Exchange Rates API](https://exchangeratesapi.io/) to fetch the latest currency exchange rates. I recommend the free plan (27/11/2024), as 100 per month is pretty good (for one user of course), and it gives access to plentiful conversions, plus it includes good documentation. When I started, I fetched the api every 12 hours.
    
Add your API key to `local.properties`:

```        
EXCHANGE_RATES_API_KEY=your_api_key
```      
        
3.  **Build and Run**:
    -   Open the project in Android Studio.
    -   Run the app on an emulator or physical device.
      
4.  **Add the Widget**:
    -   Long-press on the home screen and select "Widgets".
    -   Drag the "Currency Exchange" widget to your home screen.


<p align="right">(<a href="#readme-top">back to top</a>)</p>
<br>
<br>
Used this [template](https://github.com/othneildrew/Best-README-Template/blob/main/README.md) to create this readme.

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[license-shield]: https://img.shields.io/github/license/othneildrew/Best-README-Template.svg?style=for-the-badge
[license-url]: https://github.com/GLandeira/AndroidCurrencyExchangeWidget/blob/develop/License.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/gast%C3%B3n-landeira-garrone-90070b170/
[AndroidStudio.com]: https://img.shields.io/badge/Android%20Studio-3DDC84?style=flat&logo=AndroidStudio&logoColor=white
[AndroidStudio-url]: https://developer.android.com/studio
[Kotlin.com]: https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=Kotlin&logoColor=white
[Kotlin-url]: https://developer.android.com/kotlin
