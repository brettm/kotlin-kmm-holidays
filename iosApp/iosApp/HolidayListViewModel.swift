import Foundation
import shared

class HolidayListViewModel: ViewModel<[String: [PublicHolidayV3Dto]]> {
    
    private let holidayClient = HolidayApiFactory.companion.createPublicHolidayApi()
    private let holidaySorter = PublicHolidaySorter()
    
    nonisolated override func fetchContent() async throws -> [String: [PublicHolidayV3Dto]] {
        let holidays = try await self.fetchResponse {
            try await self.holidayClient.nextPublicHolidaysWorldwide()
        }
        return holidaySorter.groupHolidays(holidays: holidays as! [PublicHolidayV3Dto])
    }
}
