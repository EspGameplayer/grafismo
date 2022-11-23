import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { StadiumComponent } from './list/stadium.component';
import { StadiumDetailComponent } from './detail/stadium-detail.component';
import { StadiumUpdateComponent } from './update/stadium-update.component';
import { StadiumDeleteDialogComponent } from './delete/stadium-delete-dialog.component';
import { StadiumRoutingModule } from './route/stadium-routing.module';

@NgModule({
  imports: [SharedModule, StadiumRoutingModule],
  declarations: [StadiumComponent, StadiumDetailComponent, StadiumUpdateComponent, StadiumDeleteDialogComponent],
  entryComponents: [StadiumDeleteDialogComponent],
})
export class StadiumModule {}
