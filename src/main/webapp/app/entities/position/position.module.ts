import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PositionComponent } from './list/position.component';
import { PositionDetailComponent } from './detail/position-detail.component';
import { PositionUpdateComponent } from './update/position-update.component';
import { PositionDeleteDialogComponent } from './delete/position-delete-dialog.component';
import { PositionRoutingModule } from './route/position-routing.module';

@NgModule({
  imports: [SharedModule, PositionRoutingModule],
  declarations: [PositionComponent, PositionDetailComponent, PositionUpdateComponent, PositionDeleteDialogComponent],
  entryComponents: [PositionDeleteDialogComponent],
})
export class PositionModule {}
