import SwiftUI
import shared

@MainActor
protocol ViewModelInterface: AnyObject {
    associatedtype Content: Any
    var uiState: UIState<Content> { get set }
    func updateContent()
}

protocol ContentFetching: ViewModelInterface {
    associatedtype Response
    nonisolated func fetchContent() async throws -> Content
    nonisolated func fetchResponse<Response>(client: () async throws -> HttpResponse<Response>) async throws -> Response
}

extension ContentFetching {
    nonisolated func fetchResponse<Response>(client: () async throws -> HttpResponse<Response>) async throws -> Response {
        let response = try await client()
        if response.success {
            return try await response.body()
        } else {
            throw NSError(domain: String(response.status), code: Int(response.status))
        }
    }
    @MainActor
    func updateContent() {
        uiState = UIState.loading(uiState.content())
        Task {
            do {
                let content = try await fetchContent()
                Task { @MainActor in
                    uiState = UIState.loaded(content)
                }
            }
            catch {
                Task { @MainActor in
                    uiState = UIState.error(error.localizedDescription)
                }
            }
        }
    }
}
