import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ShirtComponent } from './list/shirt.component';
import { ShirtDetailComponent } from './detail/shirt-detail.component';
import { ShirtUpdateComponent } from './update/shirt-update.component';
import { ShirtDeleteDialogComponent } from './delete/shirt-delete-dialog.component';
import { ShirtRoutingModule } from './route/shirt-routing.module';

@NgModule({
  imports: [SharedModule, ShirtRoutingModule],
  declarations: [ShirtComponent, ShirtDetailComponent, ShirtUpdateComponent, ShirtDeleteDialogComponent],
  entryComponents: [ShirtDeleteDialogComponent],
})
export class ShirtModule {}
