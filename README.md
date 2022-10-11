# MovieApp
Android Application consisting of only 1 Activity and 2 fragments when the user opens the app should check the network connectivity and if the device is connected to the internet then get the most popular and top-rated movies. when the user taps on any movie we will transition to the movie details screen.
# Tech Stack
- Clean Architecture with MVVM
- Modularity
- Coroutines
- Unit test
- Handle Configuration Changes
- Jetpack Navigation component
- MenuHost
- Dependency injection design pattern
- Repository design pattern
- LiveData, ViewModel
- Retrofit
- DataBinding
- Recycler view
- Picasso for Image Loading
- ConstraintLayout
# Screen recording 📸
https://user-images.githubusercontent.com/64506232/194977688-b2323ccf-e883-4f04-8272-9bab38f4f2b2.mp4
# Project Diagram
![MovieApp Diagram drawio (2)](https://user-images.githubusercontent.com/64506232/194974977-beaf5326-f48a-4bb4-893b-a8f71ddb9397.png)
# Code Design
![Untitled Diagram (1) drawio (2)](https://user-images.githubusercontent.com/64506232/194975216-11c6b5e9-b483-4fa4-9ddc-52ac2c431eb5.png)
# Usecases
- GetMostPopularMoviesUseCase
- GetTopRatedMoviesUseCase
# Test Cases 🧪
- getMostPopularMovies() must return NetworkError DataState when the device is not connected to the internet
- getMostPopularMovies() must return Successfully DataState with list of ResultsDomainEntity when Successful API response
- getMostPopularMovies() must return Empty DataState when response return empty list
- getMostPopularMovies() must return Exception DataState when request exception
- getTopRatedMovies() must return NetworkError DataState when the device is not connected to the internet
- getTopRatedMovies() must return Successfully DataState with list of ResultsDomainEntity when Successful API response
- getTopRatedMovies() must return Empty DataState when response return empty list
- getTopRatedMovies() must return Exception DataState when request exception
- GetMostPopularMoviesUseCase() must invoke getMostPopularMovies from MoviesRepository
- GetTopRatedMoviesUseCase() must invoke getTopRatedMovies from MoviesRepository
- Map DataEntity to DomainEntity must DomainEntity
- resolvePosterPath must return full ImageUrl (BaseUrl + image size + PosterPath)
