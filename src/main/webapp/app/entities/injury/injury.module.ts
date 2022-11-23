import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InjuryComponent } from './list/injury.component';
import { InjuryDetailComponent } from './detail/injury-detail.component';
import { InjuryUpdateComponent } from './update/injury-update.component';
import { InjuryDeleteDialogComponent } from './delete/injury-delete-dialog.component';
import { InjuryRoutingModule } from './route/injury-routing.module';

@NgModule({
  imports: [SharedModule, InjuryRoutingModule],
  declarations: [InjuryComponent, InjuryDetailComponent, InjuryUpdateComponent, InjuryDeleteDialogComponent],
  entryComponents: [InjuryDeleteDialogComponent],
})
export class InjuryModule {}
