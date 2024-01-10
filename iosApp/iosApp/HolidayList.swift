import SwiftUI
import shared

struct HolidayListView: View {
    
    var viewModel: any HolidayListViewModelInterface
    
    var body: some View {
        ZStack {
            List {
                if let content = viewModel.holidays {
                    holidayList(content)
                }
            }
            .refreshable(action: { Task{ viewModel.updateContent() } })
            if case .loading = viewModel.uiState { ProgressView() }
            if case .error(let message) = viewModel.uiState {
                VStack {
                    Text(message)
                    Button("Try Again") { 
                        viewModel.updateContent()
                    }
                }
            }
        }
        .task {
            viewModel.updateContent()
        }
    }
    
    @ViewBuilder
    func holidayList(_ holidays: [String: [PublicHolidayV3Dto]]) -> some View {
        ForEach(Array(holidays.keys.sorted(by: <)), id: \.self) { key in
            Section(key) {
                ForEach(holidays[key] ?? []) { holiday in
                    HolidayView(holiday: holiday)
                }
            }
        }
    }
}

struct HolidayView: View {
    let holiday: PublicHolidayV3Dto
    var body: some View {
        HStack {
            Text(EmojiFlag().unicode(countryCode: holiday.countryCode)).font(.largeTitle)
            Text("\(holiday.name ?? "?")\n(\(holiday.localName ?? ""))")
        }
    }
}

struct HolidayListView_Previews: PreviewProvider {
    static var previews: some View {
        CountryInfoView(viewModel: Globals.countryViewModel)
    }
}
