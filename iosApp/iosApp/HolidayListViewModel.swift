import Foundation
import shared

@MainActor
protocol HolidayListViewModelInterface: ViewModelInterface where Content == [String : [PublicHolidayV3Dto]] {
    var holidays: Content? { get }
}

@Observable
@MainActor
class HolidayListViewModel: HolidayListViewModelInterface, ContentFetching {
    typealias Content = [String: [PublicHolidayV3Dto]]
    typealias Response = [PublicHolidayV3Dto]
    
    public var uiState: UIState<Content> = UIState.loading(nil)

    public var holidays: [String: [PublicHolidayV3Dto]]? {
        return uiState.content()
    }
    
    private let holidayClient = HolidayApiFactory.companion.createPublicHolidayApi()
    private let holidaySorter = PublicHolidaySorter()
    
    nonisolated func fetchContent() async throws -> Content {
        let holidays = try await self.fetchResponse {
            try await self.holidayClient.nextPublicHolidaysWorldwide()
        }
        return holidaySorter.groupHolidays(holidays: holidays as! Response)
    }
}
