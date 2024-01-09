import SwiftUI
import shared

struct CountryInfoView: View {
    var viewModel: CountryInfoViewModel
    
    var body: some View {
        ZStack {
            List {
                if let countryInfo = viewModel.countryInfo {
                    self.countryInfo(countryInfo)
                }
                if let holidays = viewModel.holidays {
                    ForEach(holidays) { holiday in
                        self.countryHoliday(holiday)
                    }
                }
            }
            .refreshable(action: viewModel.updateContent)
            if case .loading = viewModel.uiState { ProgressView() }
            if case .error(let message) = viewModel.uiState {
                VStack {
                    Text(message)
                    Button("Try Again") { viewModel.updateContent() }
                }
            }
        }
        .task {
            viewModel.updateContent()
        }
    }
    
    @ViewBuilder
    private func infoRow(_ title: String?, _ value: String?) -> some View {
        if let title, let value {
            HStack {
                Text(title).font(.callout)
                Spacer()
                Text(value)
                    .frame(maxWidth: .infinity, alignment: .trailing)
                    .multilineTextAlignment(.trailing)
            }
        }
    }
    
    @ViewBuilder
    private func countryInfo(_ info: CountryInfoDto) -> some View {
        if let name = info.commonName {
            HStack {
                Text(name).font(.headline)
                Spacer()
                Text(EmojiFlag().unicode(countryCode: info.countryCode))
            }
        }
        infoRow("Official Name", info.officialName)
        infoRow("Country Code", info.countryCode)
        infoRow("Region", info.region)
        if let borders = info.borders {
            Section("Borders") {
                ForEach(borders, id:\.countryCode){ border in
                    infoRow(border.officialName, EmojiFlag().unicode(countryCode: border.countryCode))
                }
            }
        }
    }
    
    @ViewBuilder
    private func countryHoliday(_ holiday: PublicHolidayV3Dto) -> some View {
        HStack {
            Text(holiday.date ?? "")
            Spacer()
            Text("\(holiday.name ?? "?")\n(\(holiday.localName ?? ""))")
                .multilineTextAlignment(.trailing)
        }
    }
}

struct CountryInfoView_Previews: PreviewProvider {
    static var previews: some View {
        CountryInfoView(viewModel: Globals.countryViewModel)
    }
}
