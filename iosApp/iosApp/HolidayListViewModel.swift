import Foundation
import shared

@MainActor
protocol HolidayListViewModelInterface: 
    ViewModelInterface where Content == [String: [PublicHolidayV3Dto]] 
{
    associatedtype Content
}

@MainActor
@Observable
class HolidayListViewModel: HolidayListViewModelInterface {
    
    var uiState: UIState<Content> = .notLoaded
   
    private let holidayClient = HolidayApiFactory.companion.createPublicHolidayApi()
    private let holidaySorter = PublicHolidaySorter()
    
    @Sendable nonisolated func updateContent() {
        Task {
            await updateHolidayState()
        }
    }
    
    nonisolated private func updateHolidayState() async {
        Task { @MainActor in uiState = UIState.loading(uiState.content()) }
        let nextState = await holidayInfoState()
        Task { @MainActor in uiState = nextState }
    }

    nonisolated private func holidayInfoState() async -> UIState<Content> {
       do {
           let response = try await holidayClient.nextPublicHolidaysWorldwide()
           if response.success {
               let info = try await response.body() as! [PublicHolidayV3Dto]
               return UIState.loaded(holidaySorter.groupHolidays(holidays: info))
           } else { return .error(String(response.status)) }
       } catch { return .error(error.localizedDescription) }
    }
}
