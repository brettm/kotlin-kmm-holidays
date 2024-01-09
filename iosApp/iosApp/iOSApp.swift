import SwiftUI

@MainActor
enum Globals {
  static let countryViewModel = CountryInfoViewModel()
  static let holidayViewModel = HolidayListViewModel()
}

@main
struct iOSApp: App {
    @State var selectedTab: Int = 1
	var body: some Scene {
		WindowGroup {
            TabView(selection: $selectedTab) {
                Group {
                    CountryInfoView(viewModel: Globals.countryViewModel)
                }
                .tabItem{
                    Label("My Country", systemImage: "person")
                }
                .tag(1)
                Group {
                    HolidayListView(viewModel: Globals.holidayViewModel)
                }
                .tabItem{
                    Label("Worldwide", systemImage: "globe")
                }
                .tag(2)
            }
		}
	}
}
