import SwiftUI
import shared


@MainActor
protocol ViewModelInterface {
    associatedtype Content
    var uiState: UIState<Content> { get set }
    func updateContent()
}

@Observable
class ViewModel<T>: ViewModelInterface {
    typealias Content = T
    
    var uiState: UIState<T> = .loading(nil)
    
    var content: T? {
        uiState.content()
    }
    
    func updateContent() {
        uiState = UIState.loading(uiState.content())
        Task {
            do {
                let content = try await self.fetchContent()
                Task { @MainActor in
                    uiState = UIState.loaded(content)
                }
            }
            catch { Task { @MainActor in
                uiState = UIState.error(error.localizedDescription)
            } }
        }
    }
    
    nonisolated func fetchContent() async throws -> T {
        throw NSError(domain: "Implement in subclass", code: 0)
    }
    
    nonisolated func fetchResponse<R>(client: () async throws -> HttpResponse<R>) async throws -> R {
        let response = try await client()
        if response.success { 
            return try await response.body()
        } else {
            throw NSError(domain: String(response.status), code: Int(response.status))
        }
    }
}
