import Foundation
import shared

enum CountryInfoContentKey: String {
    case info
    case holidays
}

@MainActor
protocol CountryInfoViewModelInterface: ViewModelInterface where Content == [CountryInfoContentKey: Any] {
    associatedtype Content
}

@MainActor
@Observable
class CountryInfoViewModel: CountryInfoViewModelInterface {
    
    public var uiState: UIState<Content> = .notLoaded
    public var countryInfo: CountryInfoDto? {
        return uiState.content()?[.info] as! CountryInfoDto?
    }
    public var holidays: [PublicHolidayV3Dto]? {
        return uiState.content()?[.holidays] as! [PublicHolidayV3Dto]?
    }
    
    private let countryClient = HolidayApiFactory.companion.createCountryApi()
    private let holidayClient = HolidayApiFactory.companion.createPublicHolidayApi()
    
    @Sendable nonisolated func updateContent() {
        Task {
            await updateState(nextState: countryInfoState)
            await updateState(nextState: countryHolidaysState)
        }
    }
    
    nonisolated private func updateState(nextState: () async -> UIState<Content>) async {
        Task { @MainActor in uiState = UIState.loading(uiState.content()) }
        let nextState = await nextState()
        Task { @MainActor in uiState = nextState }
    }
    
    nonisolated private func fetchContent<T>(
        mergingContentKey key: CountryInfoContentKey,
        clientResponse: () async throws -> HttpResponse<T>) async -> UIState<Content>
    {
        do {
            let response = try await clientResponse()
            if response.success {
                let info = try await response.body()
                let content = await uiState.content() ?? [:]
                return .loaded( content.merging([key: info]){ _, new in new } )
            } else { return .error(String(response.status)) }
        } catch { return .error(error.localizedDescription) }
    }
    
    nonisolated private func countryInfoState() async -> UIState<Content> {
        return await fetchContent(mergingContentKey: .info) {
            try await countryClient.countryInfoUsingDeviceLocale()
        }
    }
    
    nonisolated private func countryHolidaysState() async -> UIState<Content>{
        return await fetchContent(mergingContentKey: .holidays) {
            try await holidayClient.nextPublicHolidaysUsingDeviceLocale()
        }
    }
}
