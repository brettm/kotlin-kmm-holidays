import Foundation

enum UIState<Content> {
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
