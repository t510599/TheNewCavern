package personal.secminhr.cavern.main.ui.views.articles

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import stoneapp.secminhr.cavern.cavernObject.ArticlePreview


@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun ArticleList(list: SnapshotStateList<ArticlePreview>,
                state: LazyListState,
                onLikeClicked: (Int) -> Unit = {},
                onItemClicked: (ArticlePreview) -> Unit = {}
) {
    if(list.isNullOrEmpty()) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else {
        LazyColumn(state = state) {
            items(items = list) {
                ArticlePreviewItem(
                    preview = it,
                    onLikeClicked = onLikeClicked,
                    onItemClicked = onItemClicked
                )
                Divider()
            }
        }
    }
}


//@Preview(showBackground = true, name = "List")
//@Composable
//fun List() {
//    ArticleList(
//        list = listOf(
//            articlePreviewNormalDislike, articlePreviewNormalLike
//        )
//    )
//}
//
//@Preview(showBackground = true, name = "Loading")
//@Composable
//fun LoadingPreview() {
//    ArticleList(list = listOf())
//}