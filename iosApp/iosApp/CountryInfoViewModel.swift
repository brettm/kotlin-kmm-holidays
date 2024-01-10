import Foundation
import shared

enum CountryInfoContentKey: String {
    case info
    case holidays
}

@MainActor
protocol CountryViewModelInterface: ViewModelInterface where Content == [CountryInfoContentKey : Any] {
    var countryInfo: CountryInfoDto? { get }
    var holidays: [PublicHolidayV3Dto]? { get }
}

@Observable
@MainActor
class CountryInfoViewModel: CountryViewModelInterface, ContentFetching {

    typealias Content = [CountryInfoContentKey : Any]
    typealias Response = Any
    
    public var uiState: UIState<Content> = UIState.loading(nil)
        
    public var countryInfo: CountryInfoDto? {
        return uiState.content()?[.info] as! CountryInfoDto?
    }
    public var holidays: [PublicHolidayV3Dto]? {
        return uiState.content()?[.holidays] as! [PublicHolidayV3Dto]?
    }
    
    private let countryClient = HolidayApiFactory.companion.createCountryApi()
    private let holidayClient = HolidayApiFactory.companion.createPublicHolidayApi()
    
    nonisolated func fetchContent() async throws -> Content {
        return try await withThrowingTaskGroup(of: Content.self) { group in

            group.addTask{
                let countryInfo = try await self.fetchResponse {
                    try await self.countryClient.countryInfoUsingDeviceLocale()
                }
                return [CountryInfoContentKey.info: countryInfo]
            }
            group.addTask{
                let holidays = try await self.fetchResponse {
                    try await self.holidayClient.nextPublicHolidaysUsingDeviceLocale()
                }
                return [CountryInfoContentKey.holidays: holidays]
            }
        
            var content: [CountryInfoContentKey : Any] = [:]
            for try await value in group {
                content.merge(value, uniquingKeysWith: {_, new in new})
            }
            return content
        }
    }
}
