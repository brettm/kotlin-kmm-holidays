import SwiftUI
import shared

struct HolidayListView: View {
    
    var viewModel: HolidayListViewModel
    
    var body: some View {
        ZStack {
            List {
                if let content = viewModel.content {
                    ForEach(Array(content.keys.sorted(by: <)), id: \.self) { key in
                        Section(key) {
                            ForEach(content[key] ?? []) { holiday in
                                HolidayView(holiday: holiday)
                            }
                        }
                    }
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
