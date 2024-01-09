import Foundation
import shared

enum CountryInfoContentKey: String {
    case info
    case holidays
}

@MainActor
class CountryInfoViewModel: ViewModel<[CountryInfoContentKey: Any]> {
    
    public var countryInfo: CountryInfoDto? {
        return uiState.content()?[.info] as! CountryInfoDto?
    }
    public var holidays: [PublicHolidayV3Dto]? {
        return uiState.content()?[.holidays] as! [PublicHolidayV3Dto]?
    }
    
    private let countryClient = HolidayApiFactory.companion.createCountryApi()
    private let holidayClient = HolidayApiFactory.companion.createPublicHolidayApi()
    
    nonisolated override func fetchContent() async throws -> [CountryInfoContentKey : Any] {
        return try await withThrowingTaskGroup(of: [CountryInfoContentKey : Any].self) { group in
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
