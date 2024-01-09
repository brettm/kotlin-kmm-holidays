import SwiftUI
import shared

enum UIState<Content> {
    case notLoaded
    case loading(Content?)
    case loaded(Content)
    case error(String)

    func content() -> Content? {
        switch self {
        case .loading(let content): return content
        case .loaded(let content): return content
        default: return nil
        }
    }
}

@MainActor
protocol ViewModelInterface {
    associatedtype Content
    var uiState: UIState<Content> { get set }
    @Sendable nonisolated func updateContent()
}
